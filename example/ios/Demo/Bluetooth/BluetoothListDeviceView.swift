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

struct BluetoothListDeviceView: View, Equatable {
    static func == (lhs: BluetoothListDeviceView, rhs: BluetoothListDeviceView) -> Bool {
        lhs.viewModel.identifierString == rhs.viewModel.identifierString
    }
    
    let viewModel: BluetoothListDeviceViewModel
    
    @ObservedObject private var name: StringObservable
    @ObservedObject private var rssi: StringObservable
    @ObservedObject private var isTxPowerVisible: BoolObservable
    @ObservedObject private var txPower: StringObservable
    @ObservedObject private var status: StringObservable
    
    @ObservedObject private var isConnectButtonVisible: BoolObservable
    @ObservedObject private var connectButton: ButtonObservable
    
    @ObservedObject private var isFoldedOut: BoolObservable
    @ObservedObject private var serviceUUIDs: StringObservable
    @ObservedObject private var serviceData: StringObservable
    @ObservedObject private var manufacturerId: StringObservable
    @ObservedObject private var manufacturerData: StringObservable
    @ObservedObject private var isMoreButtonVisible: BoolObservable
    
    init(viewModel: BluetoothListDeviceViewModel) {
        self.viewModel = viewModel
        
        name = StringObservable(viewModel.name)
        rssi = StringObservable(viewModel.rssi)
        isTxPowerVisible = BoolObservable(viewModel.isTxPowerVisible)
        txPower = StringObservable(viewModel.txPower)
        status = StringObservable(viewModel.status)
        
        isConnectButtonVisible = BoolObservable(viewModel.isConnectButtonVisible)
        connectButton = ButtonObservable(viewModel.connectButton)
        
        isFoldedOut = BoolObservable(viewModel.isFoldedOut)
        serviceUUIDs = StringObservable(viewModel.serviceUUIDs)
        serviceData = StringObservable(viewModel.serviceData)
        manufacturerId = StringObservable(viewModel.manufacturerId)
        manufacturerData = StringObservable(viewModel.manufacturerData)
        isMoreButtonVisible = BoolObservable(viewModel.isMoreButtonVisible)
    }
    
    var body: some View {
        VStack {
            HStack {
                VStack(alignment: .leading) {
                    Text(name.value).font(.system(size: 15.0))
                    HStack {
                        Text(viewModel.identifierString).font(.system(size: 12.0))
                        Spacer()
                        Text(rssi.value).font(.system(size: 12.0))
                        if isTxPowerVisible.value {
                            Spacer()
                            Text(txPower.value).font(.system(size: 12.0))
                        }
                    }
                    Text(status.value).font(.system(size: 12.0)).opacity(0.8)
                }
                if isConnectButtonVisible.value {
                    connectButton.value.toButton()
                }
            }
            if isFoldedOut.value {
                VStack(alignment: .leading) {
                    Text(serviceUUIDs.value).font(.system(size: 12.0)).opacity(0.8)
                    Text(serviceData.value).font(.system(size: 12.0)).opacity(0.8)
                    Text(manufacturerId.value).font(.system(size: 12.0)).opacity(0.8)
                    Text(manufacturerData.value).font(.system(size: 12.0)).opacity(0.8)
                    if isMoreButtonVisible.value {
                        HStack {
                            Spacer()
                            Button("bluetooth_more".localized()) {
                                viewModel.onMorePressed()
                            }
                        }
                    }
                }.padding(8)
                    .frame(maxWidth: .infinity, alignment: .leading)
                .background(Color("darker_color"))
            }
        }.onTapGesture {
            viewModel.toggleFoldOut()
        }
    }
}
