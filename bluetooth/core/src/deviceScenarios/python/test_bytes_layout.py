"""Runs the byte vectors of the control protocols through the schema reader: python -m unittest test_bytes_layout"""

import json
import os
import unittest

from bytes_layout import Schema, UnsupportedLayout

HERE = os.path.dirname(os.path.abspath(__file__))


def with_bytes(value, decoded):
    """The vector's value with its byte lists, which the vector writes as hex, as bytes where the decoded value has bytes."""
    if isinstance(decoded, bytes):
        return bytes.fromhex(value)
    if isinstance(decoded, dict):
        return {key: with_bytes(value[key], decoded[key]) if key in value else value.get(key) for key in decoded}
    return value


class VectorsTest(unittest.TestCase):
    schema = Schema.load(os.path.join(HERE, "bytes-layouts.json"))
    with open(os.path.join(HERE, "bytes-layouts-vectors.json")) as file:
        vectors = json.load(file)["vectors"]

    def test_decode(self):
        for vector in self.vectors:
            with self.subTest(vector["name"]):
                decoded = self.schema.decode(vector["layout"], bytes.fromhex(vector["hex"]))
                self.assertEqual(with_bytes(vector["value"], decoded), decoded)

    def test_encode(self):
        for vector in self.vectors:
            with self.subTest(vector["name"]):
                decoded = self.schema.decode(vector["layout"], bytes.fromhex(vector["hex"]))
                value = with_bytes(vector["value"], decoded)
                self.assertEqual(bytes.fromhex(vector["hex"]), self.schema.encode(vector["layout"], value))

    def test_every_option_has_a_vector(self):
        named = {vector["name"].split(".")[1] for vector in self.vectors}
        for layout in ("Command", "Instruction", "Report"):
            root = self.schema.layouts.get(layout) or self.schema.forms[layout]
            root = self.schema.forms[root["form"]] if "form" in root else root
            for option in root["type"]["options"]:
                self.assertIn(option["name"], named, f"{layout}.{option['name']}")

    def test_unknown_construct(self):
        with open(os.path.join(HERE, "bytes-layouts.json")) as file:
            document = json.load(file)
        document["layouts"]["Command"]["type"]["options"][0]["type"]["fields"].append({"name": "flag", "bits": [0, 1], "type": {"kind": "bool"}})
        with self.assertRaises(UnsupportedLayout):
            Schema(document)


if __name__ == "__main__":
    unittest.main()
