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

struct BluetoothDeviceView : View, Equatable {
    
    static func == (lhs: BluetoothDeviceView, rhs: BluetoothDeviceView) -> Bool {
        lhs.identifier == rhs.identifier
    }
    
    @EnvironmentObject var deviceRoutingState: IdentifiableObjectRoutingState<BluetoothDeviceListView.DeviceRoute>
    @ObservedObject var name: StringObservable
    @ObservedObject var rssi: StringObservable
    @ObservedObject var distance: StringObservable
    @ObservedObject var state: StringObservable
    
    let identifier: UUID
    let lifecycleViewModel: LifecycleViewModel<BluetoothDeviceViewModel>
    let router = Router()
    
    
    init(identifier: UUID) {
        self.identifier = identifier
        let viewModel = BluetoothDeviceViewModel(identifier: identifier, navigator: router.navigator)
        lifecycleViewModel = LifecycleViewModel(viewModel)
        
        name = StringObservable(viewModel.name)
        rssi = StringObservable(viewModel.rssi)
        distance = StringObservable(viewModel.distance)
        state = StringObservable(viewModel.state)
    }
    
    var body: some View {
        router.deviceRoutingState = deviceRoutingState
        return generateBody()
    }
    
    func generateBody() -> some View {
        lifecycleViewModel.lifecycleView { viewModel in
            VStack {
                HStack {
                    Spacer()
                    Text("\(name.value) - \(identifier.uuidString)")
                    Spacer()
                    Button("Close") {
                        viewModel.close()
                    }
                }
                HStack {
                    Text("RSSI: \(rssi.value)")
                    Text("Distance: \(distance.value)")
                    Text("State: \(state.value)")
                }
                HeartRateView(heartRateViewModel: viewModel.heartRateViewModel)
                
            }
        }
    }
}

extension BluetoothDeviceView {
    class Router {
        var deviceRoutingState: IdentifiableObjectRoutingState<BluetoothDeviceListView.DeviceRoute>?
        lazy var navigator = DefaultNavigator<BluetoothDeviceViewModel.CloseNavigationAction> { actiom in
            self.deviceRoutingState?.close()
        }
    }
}

struct HeartRateView : View {
    
    @ObservedObject var heartRate: PlainLabelObservable
    @ObservedObject var isEnergyExpendedVisible: BoolObservable
    @ObservedObject var energyExpended: PlainLabelObservable
    @ObservedObject var isPositionVisible: BoolObservable
    @ObservedObject var position: PlainLabelObservable
    
    let heartRateViewModel: BluetoothDeviceViewModel.HeartRateViewModel
    
    init(heartRateViewModel: BluetoothDeviceViewModel.HeartRateViewModel) {
        self.heartRateViewModel = heartRateViewModel
        
        heartRate = PlainLabelObservable(heartRateViewModel.heartRate)
        isEnergyExpendedVisible = BoolObservable(heartRateViewModel.isEnergyExpendedVisible)
        energyExpended = PlainLabelObservable(heartRateViewModel.energyExpended)
        isPositionVisible = BoolObservable(heartRateViewModel.isPositionVisible)
        position = PlainLabelObservable(heartRateViewModel.position)
    }
    
    var body: some View {
        VStack(spacing: 8) {
            heartRate.value.toText()
            if isEnergyExpendedVisible.value {
                HStack {
                    energyExpended.value.toText()
                    Spacer()
                    heartRateViewModel.resetEnergyExpandedButton.toButton()
                }
            }
            if isPositionVisible.value {
                HStack {
                    position.value.toText()
                    Spacer()
                    heartRateViewModel.refreshPositionButton.toButton()
                }
            }
        }
    }
}
