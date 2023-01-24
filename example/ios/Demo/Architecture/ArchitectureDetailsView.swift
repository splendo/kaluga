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

struct ArchitectureDetailsView: View, Equatable {
    static func == (lhs: ArchitectureDetailsView, rhs: ArchitectureDetailsView) -> Bool {
        lhs.inputDetails == rhs.inputDetails
    }

    let inputDetails: InputDetails
    private let lifecycleViewModel: LifecycleViewModel<ArchitectureDetailsViewModel>
    let router: Router

    @ObservedObject var name: StringObservable
    @ObservedObject var number: StringObservable

    @EnvironmentObject var previousRouter: RoutingState

    init(inputDetails: InputDetails, onFinish: @escaping (InputDetails) -> Void) {
        let router = Router(onFinish: onFinish)
        self.router = router
        self.inputDetails = inputDetails
        let viewModel = ArchitectureDetailsViewModel(initialDetail: inputDetails, navigator: router.navigator)

        name = StringObservable(viewModel.name)
        number = StringObservable(viewModel.number)

        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        router.previousRouter = previousRouter
        return generateBody()
    }

    func generateBody() -> some View {
        lifecycleViewModel.lifecycleView { viewModel in
            ScrollView {
                VStack(spacing: 10.0) {
                    Text(name.value)
                    Text(number.value)
                    viewModel.inverseButton.toButton(buttonFrame: .frame(maxWidth: .infinity))
                    viewModel.finishButton.toButton(buttonFrame: .frame(maxWidth: .infinity))
                }
            }
            .padding(10.0)
            .navigationBarItems(
                leading: Button(
                    action: { viewModel.onBackPressed() },
                    label: {
                        Image(systemName: "chevron.left")
                            .scaleEffect(0.83)
                            .font(Font.title.weight(.medium))
                    }
                )
            )
            .navigationBarBackButtonHidden(true)
        }
    }
}

extension ArchitectureDetailsView {
    class Router {
        var previousRouter: RoutingState?
        let onFinish: (InputDetails) -> Void
        lazy var navigator = ArchitectureDetailsNavigatorKt.ArchitectureDetailsNavigator { [weak self] inputDetails in
            self?.onFinish(inputDetails)
            self?.previousRouter?.close()
        } onClose: { [weak self] in
            self?.previousRouter?.close()
        }
        init(onFinish: @escaping (InputDetails) -> Void) {
            self.onFinish = onFinish
        }
    }
}

struct ArchitectureDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        ArchitectureDetailsView(inputDetails: InputDetails(name: "Name", number: 42)) { _ in }
    }
}
