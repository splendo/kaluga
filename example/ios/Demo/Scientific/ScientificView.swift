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

struct ScientificView: View {

    private let lifecycleViewModel: LifecycleViewModel<ScientificViewModel>
    private let router = Router()

    @ObservedObject private var quantityDetailsButton: ButtonObservable
    @ObservedObject private var leftValue: StringSubject
    @ObservedObject private var currentLeftUnitButton: ButtonObservable
    @ObservedObject private var rightValue: StringObservable
    @ObservedObject private var currentRightUnitButton: ButtonObservable
    @ObservedObject private var converters: ListObservable<ScientificViewModel.Button>
    @ObservedObject private var route = ObjectRoutingState<Route>()
    @ObservedObject private var converterRoute = IdentifiableObjectRoutingState<ConverterRoute>()

    init() {
        let containerView = ContainerView(.alert)
        let viewModel = ScientificViewModel(alertPresenterBuilder: containerView.alertBuilder, navigator: router.navigator)

        quantityDetailsButton = ButtonObservable(viewModel.quantityDetailsButton)
        leftValue = StringSubject(viewModel.leftValue)
        currentLeftUnitButton = ButtonObservable(viewModel.currentLeftUnitButton)
        rightValue = StringObservable(viewModel.rightValue)
        currentRightUnitButton = ButtonObservable(viewModel.currentRightUnitButton)
        converters = ListObservable(viewModel.converters)

        lifecycleViewModel = LifecycleViewModel(viewModel, containerView: containerView)
    }

    var body: some View {
        router.routingState = route
        router.converterRoutingState = converterRoute
        return generateBody().navigationTitle("feature_scientific".localized())
    }

    func generateBody() -> some View {
        lifecycleViewModel.lifecycleView { viewModel in
            ScrollView {
                VStack(spacing: 10) {
                    quantityDetailsButton.value.toButton(buttonFrame: .frame(maxWidth: .infinity))
                    Text("Convert:")
                    HStack(alignment: .center, spacing: 10.0) {
                        HStack {
                            TextField("", text: $leftValue.value)
                                .textFieldStyle(.roundedBorder)
                                .frame(maxWidth: .infinity)
                            currentLeftUnitButton.value.toButton()
                        }.frame(maxWidth: .infinity)
                        HStack {
                            Text(rightValue.value)
                                .frame(maxWidth: .infinity)
                            currentRightUnitButton.value.toButton()
                        }.frame(maxWidth: .infinity)
                    }
                    viewModel.calculateButton.toButton(buttonFrame: .frame(maxWidth: .infinity))
                    Text("Converters:")
                    ForEach(converters.value) { converter in
                        converter.button.toButton(buttonFrame: .frame(maxWidth: .infinity)).navigation(
                            state: converterRoute,
                            id: converter.id,
                            type: .push,
                            didSelect: converter.button.action
                        ) { route in
                            if let object = route.object {
                                switch object {
                                case let .converter(arguments): ScientificConverterView(arguments: arguments)
                                        .equatable()
                                        .environmentObject(route as RoutingState)
                                }
                            }
                        }
                    }
                }
            }
            .padding(.horizontal, 10.0)
            .navigation(state: route, type: .sheet) { route in
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

extension ScientificView {

    enum Route: Equatable {
        case leftUnit(quantity: ScientificPhysicalQuantity)
        case rightUnit(quantity: ScientificPhysicalQuantity)
    }

    enum ConverterRoute: Identifiable, Equatable {
        var id: String {
            switch self {
            case let .converter(arguments): return arguments.id
            }
        }

        case converter(arguments: ScientificConverterViewModel.Arguments)
    }

    class Router {
        var routingState: ObjectRoutingState<Route>?
        var converterRoutingState: IdentifiableObjectRoutingState<ConverterRoute>?
        lazy var navigator = ScientificNavigatorKt.ScientificNavigator(
            onSelectLeftUnit: { [weak self] quantity in
                self?.routingState?.show(.leftUnit(quantity: quantity))
            },
            onSelectRightUnit: { [weak self] quantity in
                self?.routingState?.show(.rightUnit(quantity: quantity))
            },
            onConverter: { [weak self] arguments in
                self?.converterRoutingState?.show(.converter(arguments: arguments))
            }
        )
    }
}

extension ScientificViewModel.Button: Identifiable {}
