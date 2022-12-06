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

struct BottomSheetSubPageView: View {

    class RouteStack : ObservableObject {
        let previousRoute: RoutingState
        let parentRoute: RoutingState

        init(previousRoute: RoutingState, parentRoute: RoutingState) {
            self.previousRoute = previousRoute
            self.parentRoute = parentRoute
        }
    }

    let lifecycleViewModel: LifecycleViewModel<BottomSheetSubPageViewModel>
    private let router = Router()

    @EnvironmentObject var routeStack: RouteStack

    init() {
        let viewModel = BottomSheetSubPageViewModel(navigator: router.navigator)
        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        router.routingStack = routeStack
        return generateBody()
    }

    func generateBody() -> some View {
        lifecycleViewModel.lifecycleView { viewModel in
            VStack {
                Text(viewModel.text)
            }.padding(10.0)
                .navigationBarBackButtonHidden(true)
                .navigationBarItems(
                    leading: Button(action: { viewModel.onBackPressed() }) {
                        Image(systemName: "chevron.left")
                            .scaleEffect(0.83)
                            .font(Font.title.weight(.medium))
                    }, trailing: Button("X") {
                            viewModel.onClosePressed()
                        }
                    )
        }
    }
}

extension BottomSheetSubPageView {
    class Router {

        var routingStack: RouteStack?
        lazy var navigator = DefaultNavigator<BottomSheetSubPageNavigation> { [weak self] action in
            switch action {
            case is BottomSheetSubPageNavigation.Back: self?.routingStack?.previousRoute.close()
            case is BottomSheetSubPageNavigation.Close: self?.routingStack?.parentRoute.close()
            default: ()
            }
        }
    }
}

struct BottomSheetSubPageView_Previews: PreviewProvider {
    static var previews: some View {
        BottomSheetSubPageView()
    }
}
