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

struct TimerView: View {

    private let lifecycleViewModel: LifecycleViewModel<TimerViewModel>
    @ObservedObject var elapsed: StringObservable
    @ObservedObject var button: ButtonObservable
    
    @ObservedObject var currentTime: StringObservable
    @ObservedObject var timeZonePickerButton: ButtonObservable

    init() {
        let containerView = ContainerView(.alert)
        let viewModel = TimerViewModel(alertPresenterBuilder: containerView.alertBuilder)

        elapsed = StringObservable(viewModel.elapsed)
        button = ButtonObservable(viewModel.button)
        
        currentTime = StringObservable(viewModel.currentTime)
        timeZonePickerButton = ButtonObservable(viewModel.timeZonePickerButton)

        lifecycleViewModel = LifecycleViewModel(viewModel, containerView: containerView)
    }

    var body: some View {
        lifecycleViewModel.lifecycleView { _ in
            VStack(alignment: .center, spacing: 10.0) {
                Text(elapsed.value).font(.system(size: 32))
                button.value.toButton(buttonFrame: .frame(maxWidth: .infinity))
                Spacer().frame(height: 30)
                Text(currentTime.value).font(.system(size: 12))
                timeZonePickerButton.value.toButton(buttonFrame: .frame(maxWidth: .infinity))
                Spacer()
            }
            .padding(10.0)
            .navigationTitle(Text("feature_date_time".localized()))
        }
    }
}

struct TimerView_Previews: PreviewProvider {
    static var previews: some View {
        TimerView()
    }
}
