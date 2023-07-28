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

struct BluetoothListView: View {
    
    private let lifecycleViewModel: LifecycleViewModel<BluetoothListViewModel>
    private let router = Router()
    @ObservedObject private var deviceRoute = IdentifiableObjectRoutingState<DeviceRoute>()
    
    @ObservedObject private var title: StringObservable
    @ObservedObject private var isScanning: BoolObservable
    @ObservedObject private var pairedDevices: ListObservable<BluetoothListDeviceViewModel>
    @ObservedObject private var scannedDevices: ListObservable<BluetoothListDeviceViewModel>
    
    init() {
        let containerView = ContainerView(.alert)
        let viewModel = BluetoothListViewModel(alertPresenterBuilder: containerView.alertBuilder, navigator: router.navigator)
        
        title = StringObservable(viewModel.title)
        isScanning = BoolObservable(viewModel.isScanning)
        
        pairedDevices = ListObservable(viewModel.pairedDevices)
        scannedDevices = ListObservable(viewModel.scannedDevices)
        
        lifecycleViewModel = LifecycleViewModel(viewModel, containerView: containerView)
    }
    
    var body: some View {
        router.deviceRoutingState = deviceRoute
        return generateBody()
    }
    
    func generateBody() -> some View {
        lifecycleViewModel.lifecycleView { viewModel in
            ScrollView {
                VStack(alignment: .leading) {
                    Text("Paired").textStyle(TextStyles.shared.defaultTitle)
                    ForEach(pairedDevices.value, id: \.self) { device in
                        deviceView(device: device)
                    }
                    Text("Discovered").textStyle(TextStyles.shared.defaultTitle)
                    ForEach(scannedDevices.value, id: \.self) { device in
                        deviceView(device: device)
                    }
                }.padding(8).frame(maxWidth: .infinity, alignment: .leading)
            }.navigationTitle(title.value).navigationBarItems(
                trailing: Button(
                    isScanning.value ? "bluetooth_stop_scanning" : "bluetooth_start_scanning"
                ) { viewModel.onScanPressed() }
            )
        }
    }
    
    private func deviceView(device: BluetoothListDeviceViewModel) -> some View {
        BluetoothListDeviceView(viewModel: device)
            .navigation(
                state: deviceRoute,
                id: device.identifierString,
                type: .sheet,
                didSelect: { }
            ) {
            if let route = deviceRoute.object {
                switch route {
                case .details(let uuid): BluetoothDeviceDetailsView(identifier: uuid).equatable()
                }
            }
            }
    }
}

extension BluetoothListView {
    enum DeviceRoute: Identifiable, Equatable {
        var id: String {
            switch self {
            case .details(let uuid): return uuid.uuidString
            }
        }
        
        case details(uuid: UUID)
    }
    
    class Router {
        var deviceRoutingState: IdentifiableObjectRoutingState<DeviceRoute>?
        lazy var navigator = BluetoothListNavigatorKt.BluetoothListNavigator { uuid in
            self.deviceRoutingState?.show(.details(uuid: uuid))
        }
    }
}

struct BluetoothListView_Previews: PreviewProvider {
    static var previews: some View {
        BluetoothListView()
    }
}
