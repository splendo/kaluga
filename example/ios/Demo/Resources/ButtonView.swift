//
//  Copyright 2022 Splendo Consulting B.V. The Netherlands
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

struct ButtonView: View, Equatable {
    static func == (lhs: ButtonView, rhs: ButtonView) -> Bool {
        return true
    }

    let lifecycleViewModel: LifecycleViewModel<ButtonViewModel>
    @ObservedObject var buttons: ListObservable<KalugaButton>

    init() {
        let containerView = ContainerView(.alert)
        let viewModel = ButtonViewModel(styledStringBuilderProvider: StyledStringBuilder.Provider(), alertPresenterBuilder: containerView.alertBuilder)
        buttons = ListObservable(viewModel.buttons)
        lifecycleViewModel = LifecycleViewModel(viewModel, containerView: containerView)
    }

    var body: some View {
        lifecycleViewModel.lifecycleView { _ in
            ScrollView {
                VStack(spacing: 10.0) {
                    ForEach(buttons.value, id: \.self) { button in
                        button.toButton(buttonFrame: .frame(maxWidth: .infinity))
                    }
                }.padding(10.0)
            }.navigationTitle("feature_resources_button".localized())
        }
    }
}

struct ButtonView_Previews: PreviewProvider {
    static var previews: some View {
        ButtonView()
    }
}
