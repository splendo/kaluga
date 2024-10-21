//
//  Copyright 2022 Splendo Consulting B.V. The Netherlands
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

struct BottomSheetView: View, Equatable {
    static func == (lhs: BottomSheetView, rhs: BottomSheetView) -> Bool {
        true
    }

    let lifecycleViewModel: LifecycleViewModel<BottomSheetViewModel>
    let router = Router()

    @ObservedObject var nextRoute = RoutingState()
    @EnvironmentObject var previousRoute: RoutingState

    init() {
        let viewModel = BottomSheetViewModel(navigator: router.navigator)
        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        router.nextRoute = nextRoute
        router.previousRoute = previousRoute
        return generateBody()
    }

    func generateBody() -> some View {
        NavigationView { // Bug in partial sheet requires moving the bottom sheet a bit before it becomes interactive
            lifecycleViewModel.lifecycleView { viewModel in
                VStack(spacing: 10.0) {
                    Text(viewModel.text)
                    viewModel.button.toButton(buttonFrame: .frame(maxWidth: .infinity))
                }.padding(10.0)
                    .navigation(state: nextRoute, type: .push) { [unowned previousRoute] route in
                        BottomSheetSubPageView().environmentObject(BottomSheetSubPageView.RouteStack(previousRoute: route, parentRoute: previousRoute))
                    }
                    .navigationViewStyle(.stack)
                    .navigationBarItems(
                        trailing: Button("X") {
                            viewModel.onClosePressed()
                        }
                    )
            }
        }.frame(height: 150.0) // bug in partial sheet
    }
}

extension BottomSheetView {
    class Router {

        var nextRoute: RoutingState?
        var previousRoute: RoutingState?
        lazy var navigator = DefaultNavigator<BottomSheetNavigation> { [weak self] action in
            switch action {
            case is BottomSheetNavigation.SubPage: self?.nextRoute?.show()
            case is BottomSheetNavigation.Close: self?.previousRoute?.close()
            default: ()
            }
        }
    }
}

struct BottomSheetView_Previews: PreviewProvider {
    static var previews: some View {
        BottomSheetView()
    }
}
