//
//  Copyright 2023 Splendo Consulting B.V. The Netherlands
// 
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//

import SwiftUI
import KalugaExampleShared

struct KeyboardManagerView: View {

    enum Field : String, Hashable {
        case textField
    }

    let lifecycleViewModel: LifecycleViewModel<KeyboardViewModel<ValueFocusHandler<HashableClass<Field>>>>
    @ObservedObject var keyboardManagerBuilder: SwiftUIKeyboardManagerBuilder<Field>
    @State var text: String = ""
    @FocusState private var focusedField: Field?

    init() {
        let keyboardManagerBuilder = SwiftUIKeyboardManagerBuilder<Field>()
        self.keyboardManagerBuilder = keyboardManagerBuilder
        let viewModel = KeyboardViewModel(keyboardManagerBuilder: keyboardManagerBuilder, editFieldFocusHandler: SwiftUIFocusHandler(value: Field.textField))
        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        lifecycleViewModel.lifecycleView { viewModel in
            VStack(spacing: 10.0) {
                TextField("", text: _text.projectedValue)
                    .focused($focusedField, equals: Field.textField)
                    .textFieldStyle(.roundedBorder)
                viewModel.showButton.toButton(
                    buttonFrame: .frame(maxWidth: .infinity)
                )
                viewModel.hideButton.toButton(
                    buttonFrame: .frame(maxWidth: .infinity)
                )
                Spacer()
            }.padding(10.0)
                .navigationTitle(Text("feature_keyboard".localized())).bindKeyboardManager(keyboardManagerBuilder: keyboardManagerBuilder, focusState: _focusedField.projectedValue)
        }
    }
}

struct KeyboardManagerView_Previews: PreviewProvider {
    static var previews: some View {
        KeyboardManagerView()
    }
}




