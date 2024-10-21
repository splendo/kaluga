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
import PartialSheet

struct ArchitectureView: View {

    private let lifecycleViewModel: LifecycleViewModel<ArchitectureViewModel>
    private let router = Router()

    @ObservedObject var nameInput: StringSubject
    @ObservedObject var isNameValid: BoolObservable
    @ObservedObject var numberInput: StringSubject
    @ObservedObject var isNumberValid: BoolObservable

    @ObservedObject var contentRoutingState = ObjectRoutingState<ContentRoute>()
    @ObservedObject var bottomSheetRoutingState = RoutingState()

    init() {
        let viewModel = ArchitectureViewModel(navigator: router.defaultNavigator)
        nameInput = StringSubject(viewModel.nameInput)
        isNameValid = BoolObservable(viewModel.isNameValid)
        numberInput = StringSubject(viewModel.numberInput)
        isNumberValid = BoolObservable(viewModel.isNumberValid)
        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        router.nextContentRoutingState = contentRoutingState
        router.bottomSheetRoutingState = bottomSheetRoutingState
        return generateBody()
    }

    func generateBody() -> some View {
        lifecycleViewModel.lifecycleView { viewModel in
            ScrollView {
                VStack(spacing: 10.0) {
                    TextField(viewModel.namePlaceholder, text: $nameInput.value)
                        .textFieldStyle(.roundedBorder)
                        .border(isNameValid.value ? Color.gray : Color.red)
                    TextField(viewModel.numberPlaceholder, text: $numberInput.value)
                        .textFieldStyle(.roundedBorder)
                        .keyboardType(.numberPad)
                        .border(isNumberValid.value ? Color.gray : Color.red)
                    viewModel.showDetailsButton.toButton(buttonFrame: .frame(maxWidth: .infinity))
                    viewModel.showBottomSheetButton.toButton(buttonFrame: .frame(maxWidth: .infinity))
                }
            }
            .padding(10.0)
            .navigation(state: contentRoutingState, type: .push) { route in
                switch route.object {
                case let .details(input): ArchitectureDetailsView(inputDetails: input) { result in
                    viewModel.nameInput.post(newValue: result.name as NSString)
                    viewModel.numberInput.post(newValue: NSString.init(format: "%d", result.number))
                }.equatable()
                        .environmentObject(route as RoutingState)
                default: EmptyView()
                }
            }
            .navigation(state: bottomSheetRoutingState, type: .partialSheet(style: PSIphoneStyle.defaultStyle())) { route in
                BottomSheetView().equatable().environmentObject(route)
            }
            .navigationTitle("feature_architecture".localized())
        }
    }
}

extension ArchitectureView {
    enum ContentRoute: Equatable {
        case details(input: InputDetails)
    }

    class Router {

        var nextContentRoutingState: ObjectRoutingState<ContentRoute>?
        var bottomSheetRoutingState: RoutingState?
        lazy var defaultNavigator = ArchitectureNavigatorKt.ArchitectureNavigator(
            onDetails: { [weak self] inputDetails in
                self?.nextContentRoutingState?.show(ContentRoute.details(input: inputDetails))
            },
            onBottomSheet: { [weak self] in
                self?.bottomSheetRoutingState?.show()
            }
        )
    }
}

struct ArchitectureView_Previews: PreviewProvider {
    static var previews: some View {
        ArchitectureView()
    }
}
