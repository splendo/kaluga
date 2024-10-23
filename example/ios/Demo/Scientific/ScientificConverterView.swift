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

struct ScientificConverterView: View, Equatable {
    
    static func == (lhs: ScientificConverterView, rhs: ScientificConverterView) -> Bool {
        lhs.arguments == rhs.arguments
    }

    let arguments: ScientificConverterViewModel.Arguments
    private let lifecycleViewModel: LifecycleViewModel<ScientificConverterViewModel>
    private let router = Router()
    @ObservedObject private var leftValue: StringSubject
    @ObservedObject private var currentLeftUnitButton: ButtonObservable
    @ObservedObject private var rightValue: StringSubject
    @ObservedObject private var currentRightUnitButton: ButtonObservable
    @ObservedObject private var resultValue: StringObservable
    @ObservedObject private var nextRoute = ObjectRoutingState<Route>()
    @EnvironmentObject var previousRoute: RoutingState

    init(arguments: ScientificConverterViewModel.Arguments) {
        self.arguments = arguments

        let viewModel = ScientificConverterViewModelKt.ScientificConverterViewModel(
            arguments: arguments,
            navigator: router.navigator
        )

        leftValue = StringSubject(viewModel.leftValue)
        currentLeftUnitButton = ButtonObservable(viewModel.currentLeftUnitButton)
        rightValue = StringSubject(viewModel.rightValue)
        currentRightUnitButton = ButtonObservable(viewModel.currentRightUnitButton)
        resultValue = StringObservable(viewModel.resultValue)

        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        router.previousRoutingState = previousRoute
        router.nextRoutingState = nextRoute
        return generateBody().navigationTitle("feature_scientific".localized())
    }

    func generateBody() -> some View {
        lifecycleViewModel.lifecycleView { viewModel in
            ScrollView {
                VStack(spacing: 10) {
                    HStack(alignment: .center, spacing: 10.0) {
                        HStack {
                            TextField("", text: $leftValue.value)
                                .textFieldStyle(.roundedBorder)
                                .frame(maxWidth: .infinity)
                            currentLeftUnitButton.value.toButton()
                        }.frame(maxWidth: .infinity)
                        if viewModel.isRightUnitSelectable {
                            Text(viewModel.calculateOperatorSymbol)
                            HStack {
                                TextField("", text: $rightValue.value)
                                    .textFieldStyle(.roundedBorder)
                                    .frame(maxWidth: .infinity)
                                currentRightUnitButton.value.toButton()
                            }.frame(maxWidth: .infinity)
                        }
                    }
                    viewModel.calculateButton.toButton(buttonFrame: .frame(maxWidth: .infinity))
                    HStack {
                        Text(resultValue.value)
                    }
                }
            }
            .padding(10.0)
            .navigation(state: nextRoute, type: .sheet) { route in
                if let routeObject = route.object {
                    switch routeObject {
                    case let .leftUnit(quantity):
                        ScientificUnitSelectionView(quantity: quantity) { index in
                            viewModel.didSelectLeftUnit(unitIndex: index)
                        }
                        .equatable()
                        .environmentObject(route as RoutingState)
                    case let .rightUnit(quantity):
                        ScientificUnitSelectionView(quantity: quantity) { index in
                            viewModel.didSelectRightUnit(unitIndex: index)
                        }
                        .equatable()
                        .environmentObject(route as RoutingState)
                    }
                }
            }
        }
    }
}

extension ScientificConverterView {

    enum Route: Equatable {
        case leftUnit(quantity: ScientificPhysicalQuantity)
        case rightUnit(quantity: ScientificPhysicalQuantity)
    }

    class Router {
        var previousRoutingState: RoutingState?
        var nextRoutingState: ObjectRoutingState<Route>?
        lazy var navigator = ScientificConverterNavigatorKt.ScientificConverterNavigator(
            onSelectLeftUnit: { [weak self] quantity in
                self?.nextRoutingState?.show(.leftUnit(quantity: quantity))
            },
            onSelectRightUnit: { [weak self] quantity in
                self?.nextRoutingState?.show(.rightUnit(quantity: quantity))
            },
            onClose: { [weak self] in
                self?.previousRoutingState?.close()
            }
        )
    }
}
