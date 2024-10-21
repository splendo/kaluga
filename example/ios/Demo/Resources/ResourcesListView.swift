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

struct ResourcesListView: View {

    @ObservedObject var navigationState = ObjectRoutingState<Route>()
    @ObservedObject var resources: ListObservable<Resource>
    let lifecycleViewModel: LifecycleViewModel<ResourcesListViewModel>
    let router = Router()

    init() {

        let viewModel = ResourcesListViewModel(navigator: router.defaultNavigator)
        resources = ListObservable(viewModel.resources)
        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        router.nextRouter = navigationState
        return generateBody().navigationTitle("feature_resources".localized())
    }

    func generateBody() -> some View {
        lifecycleViewModel.lifecycleView { viewModel in
            ScrollView {
                VStack(spacing: 10.0) {
                    ForEach(resources.value, id: \.self) { resource in
                        Button(resource.title) {
                            viewModel.onResourceSelected(resource: resource)
                        }
                    }
                }
            }
            .navigation(state: navigationState, type: .push) { state in
                switch state.object {
                case .buttons: ButtonView().equatable() // For lifecycle subviews it is recommended to use equatable
                case .labels: LabelView()
                case .images: ImagesView()
                case .colors: ColorView().equatable()
                default: EmptyView()
                }
            }
        }
    }
}

extension ResourcesListView {
    enum Route: Equatable {
        case buttons
        case labels
        case images
        case colors
    }

    class Router {

        var nextRouter: ObjectRoutingState<Route>?
        lazy var defaultNavigator: DefaultNavigator<ResourcesListNavigationAction> = DefaultNavigator { [weak self] action in
            self?.nextRouter?.show(action.route)
        }
    }
}

extension ResourcesListNavigationAction {
    var route: ResourcesListView.Route {
        switch self {
        case is ResourcesListNavigationAction.Button: return .buttons
        case is ResourcesListNavigationAction.Color: return .colors
        case is ResourcesListNavigationAction.Image: return .images
        case is ResourcesListNavigationAction.Label: return .labels
        default: fatalError("Unknown action \(self)")
        }
     }
}

struct ResourcesListView_Previews: PreviewProvider {
    static var previews: some View {
        ResourcesListView()
    }
}
