"""Encodes and decodes values by a `kaluga-bytes-layout` schema, the format the kaluga bytes processor exports.

Covers what the control protocols of the device scenarios use: structures, sealed types told apart by a discriminator
(with a fallback that keeps its discriminator), whole numbers, and byte lists running to the end of the value. Any
other construct of the format raises `UnsupportedLayout`, so a schema that grows past this reader fails at start-up
rather than misreading a message.

A structure is a dict of its fields, a sealed value a dict with its option's name under `"type"` next to its fields, a
whole number an int and a byte list `bytes`.
"""

import json

FORMAT = "kaluga-bytes-layout"
VERSION = 1

# The keys this reader understands on each kind of node. A node with any other key holds a construct it does not know.
ROOT_KEYS = {"doc", "order", "type", "form"}
FIELD_KEYS = {"name", "doc", "order", "type", "form"}
OPTION_KEYS = {"name", "doc", "type"}
TYPE_KEYS = {
    "struct": {"kind", "fields"},
    "sealed": {"kind", "discriminators", "options", "fallback"},
    "natural": {"kind", "lengths", "signed"},
    "discriminator": {"kind", "lengths"},
    "list": {"kind", "marking", "item"},
}


class UnsupportedLayout(Exception):
    pass


class LayoutError(Exception):
    pass


def _check(node, allowed, where):
    unknown = set(node) - allowed
    if unknown:
        raise UnsupportedLayout(f"{where}: {sorted(unknown)}")


class Schema:
    def __init__(self, document):
        if document.get("format") != FORMAT or document.get("version") != VERSION:
            raise UnsupportedLayout(f"not a {FORMAT} schema of version {VERSION}")
        self.layouts = document["layouts"]
        self.forms = document.get("forms", {})
        for name, root in list(self.layouts.items()) + list(self.forms.items()):
            self._validate_root(root, name)

    @classmethod
    def load(cls, path):
        with open(path) as file:
            return cls(json.load(file))

    def encode(self, layout, value):
        root = self._root(self.layouts[layout])
        out = bytearray()
        self._encode(root["type"], root.get("order", "lsb"), value, out, layout)
        return bytes(out)

    def decode(self, layout, data):
        root = self._root(self.layouts[layout])
        value, end = self._decode(root["type"], root.get("order", "lsb"), bytes(data), 0, len(data), layout)
        if end != len(data):
            raise LayoutError(f"{layout}: {len(data) - end} bytes left after the value")
        return value

    def _root(self, root):
        while "form" in root:
            root = self.forms[root["form"]]
        return root

    def _validate_root(self, root, where):
        _check(root, ROOT_KEYS, where)
        if root.get("order", "lsb") not in ("lsb", "msb"):
            raise UnsupportedLayout(f"{where}: order {root['order']}")
        if "type" in root:
            self._validate_type(root["type"], where)

    def _validate_type(self, node, where):
        kind = node.get("kind")
        if kind not in TYPE_KEYS:
            raise UnsupportedLayout(f"{where}: kind {kind}")
        _check(node, TYPE_KEYS[kind], f"{where} ({kind})")
        if kind == "struct":
            for field in node["fields"]:
                self._validate_field(field, f"{where}.{field.get('name')}")
        elif kind == "sealed":
            for option in node["options"]:
                _check(option, OPTION_KEYS, f"{where}.{option.get('name')}")
                self._validate_type(option["type"], f"{where}.{option['name']}")
            for name, discriminator in node["discriminators"].items():
                if len(bytes.fromhex(discriminator)) != 1:
                    raise UnsupportedLayout(f"{where}.{name}: discriminator {discriminator}")
        elif kind in ("natural", "discriminator"):
            if len(node["lengths"]) != 1 or node["lengths"][0] % 8:
                raise UnsupportedLayout(f"{where}: lengths {node['lengths']}")
        elif kind == "list":
            item = node["item"]
            _check(item, FIELD_KEYS, f"{where} item")
            if node["marking"] != {"kind": "unmarked"} or item.get("type") != {"kind": "natural", "lengths": [8]}:
                raise UnsupportedLayout(f"{where}: only a list of unsigned bytes to the end of the value")

    def _validate_field(self, field, where):
        _check(field, FIELD_KEYS, where)
        if "type" in field:
            self._validate_type(field["type"], where)

    def _field_type(self, field, order):
        if "form" in field:
            root = self.forms[field["form"]]
            return root["type"], root.get("order", order)
        return field["type"], field.get("order", order)

    def _encode(self, node, order, value, out, where):
        kind = node["kind"]
        if kind == "struct":
            for field in node["fields"]:
                field_type, field_order = self._field_type(field, order)
                # The sealed type around the structure writes the discriminator.
                if field_type["kind"] == "discriminator":
                    continue
                self._encode(field_type, field_order, value[field["name"]], out, f"{where}.{field['name']}")
        elif kind == "sealed":
            option = self._option(node, value["type"], where)
            if value["type"] in node["discriminators"]:
                out += bytes.fromhex(node["discriminators"][value["type"]])
            else:
                discriminator = next(f for f in option["type"]["fields"] if "type" in f and f["type"]["kind"] == "discriminator")
                self._encode_number(discriminator["type"], order, value[discriminator["name"]], out, where)
            self._encode(option["type"], order, value, out, f"{where}.{value['type']}")
        elif kind == "natural":
            self._encode_number(node, order, value, out, where)
        elif kind == "list":
            out += bytes(value)

    def _encode_number(self, node, order, value, out, where):
        width = node["lengths"][0] // 8
        try:
            out += int(value).to_bytes(width, "little" if order == "lsb" else "big", signed=node.get("signed", False))
        except OverflowError as error:
            raise LayoutError(f"{where}: {value} does not fit in {width} bytes") from error

    def _option(self, node, name, where):
        for option in node["options"]:
            if option["name"] == name:
                return option
        raise LayoutError(f"{where}: no option {name}")

    def _decode(self, node, order, data, start, end, where, discriminator=None):
        kind = node["kind"]
        if kind == "struct":
            value = {}
            for field in node["fields"]:
                field_type, field_order = self._field_type(field, order)
                if field_type["kind"] == "discriminator":
                    value[field["name"]] = discriminator
                    continue
                value[field["name"]], start = self._decode(field_type, field_order, data, start, end, f"{where}.{field['name']}")
            return value, start
        if kind == "sealed":
            if start >= end:
                raise LayoutError(f"{where}: no discriminator")
            code = data[start : start + 1].hex()
            name = next((option for option, hex_code in node["discriminators"].items() if hex_code == code), None)
            if name is None:
                if "fallback" not in node:
                    raise LayoutError(f"{where}: unknown discriminator {code}")
                name = node["fallback"]
            option = self._option(node, name, where)
            fields, start = self._decode(option["type"], order, data, start + 1, end, f"{where}.{name}", data[start])
            return {"type": name, **fields}, start
        if kind == "natural":
            width = node["lengths"][0] // 8
            if start + width > end:
                raise LayoutError(f"{where}: ends after {end - start} of {width} bytes")
            number = int.from_bytes(data[start : start + width], "little" if order == "lsb" else "big", signed=node.get("signed", False))
            return number, start + width
        if kind == "list":
            return data[start:end], end
        raise UnsupportedLayout(f"{where}: kind {kind}")
