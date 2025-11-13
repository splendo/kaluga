//
//  Copyright 2025 Splendo Consulting B.V. The Netherlands
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

struct BluetoothListView: View {

    @ObservedObject var navigationState = ObjectRoutingState<Route>()
    @ObservedObject var bluetooth: ListObservable<Bluetooth>
    let lifecycleViewModel: LifecycleViewModel<BluetoothListViewModel>
    let router = Router()

    init() {

        let viewModel = BluetoothListViewModel(navigator: router.defaultNavigator)
        bluetooth = ListObservable(viewModel.bluetooth)
        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        router.nextRouter = navigationState
        return generateBody().navigationTitle("feature_bluetooth".localized())
    }

    func generateBody() -> some View {
        lifecycleViewModel.lifecycleView { viewModel in
            ScrollView {
                VStack(spacing: 10.0) {
                    ForEach(bluetooth.value, id: \.self) { bluetooth in
                        Button(bluetooth.title) {
                            viewModel.onBluetoothSelected(bluetooth: bluetooth)
                        }
                    }
                }
            }
            .navigation(state: navigationState, type: .push) { state in
                switch state.object {
                case .client: BluetoothDeviceListView()
                case .server: BluetoothServerView {
                    state.close()
                }
                default: EmptyView()
                }
            }
        }
    }
}

extension BluetoothListView {
    enum Route: Equatable {
        case client
        case server
    }

    class Router {

        var nextRouter: ObjectRoutingState<Route>?
        lazy var defaultNavigator: DefaultNavigator<BluetoothListNavigationAction> = DefaultNavigator { [weak self] action in
            self?.nextRouter?.show(action.route)
        }
    }
}

extension BluetoothListNavigationAction {
    var route: BluetoothListView.Route {
        switch self {
        case is BluetoothListNavigationAction.Server: return .server
        case is BluetoothListNavigationAction.Client: return .client
        default: fatalError("Unknown action \(self)")
        }
     }
}

struct BluetoothListView_Previews: PreviewProvider {
    static var previews: some View {
        BluetoothListView()
    }
}
