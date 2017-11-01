import {RouterModule, Routes} from "@angular/router";
//import {RegisterComponent} from "./Component/Registry/Register/Register.Component";
//import {SourceDomainComponent} from "./Component/Registry/SourceDomain/SourceDomain.Component";
//import {DestinationDomainComponent} from "./Component/Registry/DestinationDomain/DestinationDomain.Component";
import {HomeComponent} from "./Modules/Home/Home.Component";
//[IMPORT MODULE]
// import {[MODULE]Component} from "./Modules/[MODULE]/[MODULE].Component";
 import {edgeComponent} from "./Modules/edge/edge.Component";
 import {fileComponent} from "./Modules/file/file.Component";
 import {pointComponent} from "./Modules/point/point.Component";
 import {problemComponent} from "./Modules/problem/problem.Component";
 import {ShapeComponent} from "./Modules/shape/shape.Component";
 import {userComponent} from "./Modules/user/user.Component";
// import {[MODULE]Component} from "./Modules/[MODULE]/[MODULE].Component";
//[END]
const routes: Routes = [
    //{
    //    path: 'Registry/SourceDomains',
    //    component: SourceDomainComponent
    //},
    {
        path: 'Home',
        component: HomeComponent,
        // canActivate: [AuthGuard]
    },
//[IMPORT MODULE]
// {    path: '[MODULE]',    component: [MODULE]Component},
 {    path: 'edges',    component: edgeComponent},
 {    path: 'files',    component: fileComponent},
 {    path: 'points',    component: pointComponent},
 {    path: 'problems',    component: problemComponent},
 {    path: 'shapes',    component: ShapeComponent},
 {    path: 'users',    component: userComponent},
// {    path: '[MODULE]',    component: [MODULE]Component},
    //[END]
    {
        path: '**',
        redirectTo: 'Home',
    },
    {
        path: 'Fams',
        redirectTo: 'Fams/PurchaseRequest',
        pathMatch: 'full'
    },
    {
        path: 'List',
        redirectTo: 'List/Vendor',
        pathMatch: 'full'
    },
    {
        path: 'Permission',
        redirectTo: 'Permission/User',
        pathMatch: 'full'
    }
];
export const Routing = RouterModule.forRoot(routes);
