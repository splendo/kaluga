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

struct ScientificUnitSelectionView: View, Equatable {

    static func == (lhs: ScientificUnitSelectionView, rhs: ScientificUnitSelectionView) -> Bool {
        lhs.quantity == rhs.quantity
    }

    private let lifecycleViewModel: LifecycleViewModel<ScientificUnitSelectionViewModel>

    let quantity: ScientificPhysicalQuantity
    let router: Router
    @ObservedObject var filter: StringSubject
    @ObservedObject var currentUnits: ListObservable<KalugaButton.Plain>
    @EnvironmentObject var previousRoute: RoutingState

    init(quantity: ScientificPhysicalQuantity, onUnitIndex: @escaping (Int32) -> Void) {
        self.quantity = quantity
        router = Router(onUnitIndex: onUnitIndex)
        let viewModel = ScientificUnitSelectionViewModel(quantity: quantity, navigator: router.navigator)

        filter = StringSubject(viewModel.filter)
        currentUnits = ListObservable(viewModel.currentUnits)

        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        router.previousRoute = previousRoute
        return generateBody()
    }

    func generateBody() -> some View {
        lifecycleViewModel.lifecycleView { _ in
            VStack {
                HStack {
                    Text("Select Unit").padding(10.0)
                }
                ScrollView {
                    VStack {
                        TextField("", text: $filter.value)
                            .textFieldStyle(.roundedBorder)
                            .frame(maxWidth: .infinity)
                        ForEach(currentUnits.value, id: \.self) { button in
                            button.toButton(buttonFrame: .frame(maxWidth: .infinity))
                        }
                    }
                }.padding(10.0)
            }
        }
    }
}

extension ScientificUnitSelectionView {
    class Router {

        private let onUnitIndex: ((Int32) -> Void)?
        var previousRoute: RoutingState?
        lazy var navigator = ScientificUnitSelectionNavigatorKt.ScientificUnitSelectionNavigator { [weak self] index in
            self?.onUnitIndex?(index.int32Value)
            self?.previousRoute?.close()
        } onCancelled: { [weak self] in
            self?.previousRoute?.close()
        }

        init(onUnitIndex: @escaping (Int32) -> Void) {
            self.onUnitIndex = onUnitIndex
        }
    }
}
