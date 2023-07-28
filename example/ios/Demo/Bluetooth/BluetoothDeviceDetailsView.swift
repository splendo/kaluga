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

struct BluetoothDeviceDetailsView: View, Equatable {
    
    static func == (lhs: BluetoothDeviceDetailsView, rhs: BluetoothDeviceDetailsView) -> Bool {
        lhs.identifier == rhs.identifier
    }
    
    let identifier: UUID
    let lifecycleViewModel: LifecycleViewModel<BluetoothDeviceDetailViewModel>
    
    @ObservedObject private var name: StringObservable
    @ObservedObject private var rssi: StringObservable
    @ObservedObject private var distance: StringObservable
    @ObservedObject private var connectionStatus: StringObservable
    @ObservedObject private var services: ListObservable<BluetoothServiceViewModel>
    
    init(identifier: UUID) {
        self.identifier = identifier
        
        let viewModel = BluetoothDeviceDetailViewModel(identifier: identifier)
        lifecycleViewModel = LifecycleViewModel(viewModel)
        
        name = StringObservable(viewModel.name)
        rssi = StringObservable(viewModel.rssi)
        distance = StringObservable(viewModel.distance)
        connectionStatus = StringObservable(viewModel.state)
        services = ListObservable(viewModel.services)
    }
    
    var body: some View {
        lifecycleViewModel.lifecycleView { viewModel in
            VStack {
                Text(name.value).font(.system(size: 15.0))
                HStack {
                    Text(viewModel.identifierString).font(.system(size: 12.0))
                    Spacer()
                    Text(rssi.value).font(.system(size: 12.0))
                    Spacer()
                    Text(distance.value).font(.system(size: 12.0))
                }
                Text(connectionStatus.value).font(.system(size: 12.0)).opacity(0.8)
                Spacer().frame(height: 10)
                Text("Services").font(.system(size: 15.0))
                ScrollView {
                    VStack(alignment: .leading) {
                        ForEach(services.value, id: \.self) { service in
                            BluetoothServiceView(service: service)
                        }
                    }
                }
            }
        }
    }
}
