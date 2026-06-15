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

struct BluetoothServerView: View {
 
    @ObservedObject var status: PlainLabelObservable
    @ObservedObject var heartRateLabel: PlainLabelObservable
    @ObservedObject var energyExpendedLabel: PlainLabelObservable
    @ObservedObject var positionPicker: PlainButtonObservable
    let lifecycleViewModel: LifecycleViewModel<BluetoothServerViewModel>
    
    init(onClose: @escaping () -> Void) {
        let container = ContainerView(.alert)
        let viewModel = BluetoothServerViewModel(alertPresenter: container.alertBuilder, navigator: DefaultNavigator<BluetoothServerViewModel.CloseNavigationAction> { action in onClose() })
        lifecycleViewModel = LifecycleViewModel(viewModel, containerView: container)
        status = PlainLabelObservable(viewModel.status)
        heartRateLabel = PlainLabelObservable(viewModel.heartRateLabel)
        energyExpendedLabel = PlainLabelObservable(viewModel.energyExpendedLabel)
        positionPicker = PlainButtonObservable(viewModel.positionPicker)
    }
    
    var body: some View {
        lifecycleViewModel.lifecycleView { viewModel in
            VStack(alignment: .center, spacing: 8.0) {
                status.value.toText()
                HStack(alignment: .center, spacing: 10.0) {
                    viewModel.decreaseBPM.toButton()
                    heartRateLabel.value.toText()
                    viewModel.increaseBPM.toButton()
                }
                energyExpendedLabel.value.toText()
                positionPicker.value.toButton()
            }.navigationBarItems(
                leading: Button(
                    action: { viewModel.close() },
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
