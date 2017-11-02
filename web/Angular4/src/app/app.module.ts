///<reference path="../../node_modules/@angular/http/src/http_module.d.ts"/>
///<reference path="../../node_modules/primeng/components/calendar/calendar.d.ts"/>
///<reference path="Shared/MaterialComponent/Portlet/Portlet.Component.ts"/>
// Import Component
import {AppComponent} from "./app.component";
import {HeaderComponent} from "./Shared/Header/Header.Component";
import {BodyComponent} from "./Shared/Body/Body.Component";
import {DropdownComponent} from "./Shared/MaterialComponent/dropdown/dropdown.component";
import {PagingComponent} from "./Shared/MaterialComponent/paging/paging.component";
import {HttpService} from "./Shared/HttpService";
import {HomeComponent} from "./Modules/Home/Home.Component";
import {ModalComponent} from "./Shared/MaterialComponent/modal/modal.component";
import {MenuPurchaseComponent} from "./Shared/MaterialComponent/MenuPurchase/menupurchase.component";
import {TooltipDirective} from 'ng2-tooltip-directive/components';
import {NgbDateFRParserFormatter} from "./Shared/DateParseFormatter";
import {NgbDateParserFormatter, NgbModule} from '@ng-bootstrap/ng-bootstrap';
//[IMPORT MODULE]
// import {[MODULE]Component} from "./Modules/[MODULE]/[MODULE].Component";
import {edgeComponent} from "./Modules/edge/edge.Component";
import {fileComponent} from "./Modules/file/file.Component";
import {pointComponent} from "./Modules/point/point.Component";
import {problemComponent} from "./Modules/problem/problem.Component";
import {ShapeComponent} from "./Modules/shape/shape.Component";
import {userComponent} from "./Modules/user/user.Component"; // import {[MODULE]Component} from "./Modules/[MODULE]/[MODULE].Component";
//[END]
//[IMPORT MODULE]
// import {[MODULE]Service} from "./Modules/[MODULE]/[MODULE].Service";
import {EdgeService} from "./Modules/edge/edge.Service";
import {FileService} from "./Modules/file/file.Service";
import {pointService} from "./Modules/point/point.Service";
import {ProblemService} from "./Modules/problem/problem.Service";
import {ShapeService} from "./Modules/shape/shape.Service";
import {userService} from "./Modules/user/user.Service";
import {ToastModule} from "ng2-toastr";
import {NgModule, NO_ERRORS_SCHEMA} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Http, HttpModule, RequestOptions, XHRBackend} from "@angular/http";
import {Routing} from "./Route";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ConfirmationPopoverModule} from "angular-confirmation-popover";
import {
    AccordionModule,
    ButtonModule,
    CalendarModule,
    ContextMenuModule,
    DataTableModule,
    DialogModule,
    InputTextModule,
    RatingModule,
    TreeModule
} from "primeng/primeng";
import {ExcelComponent} from "./Shared/MaterialComponent/Excel/Excel.component";
import {TagsinputComponent} from "./Shared/MaterialComponent/tagsinput/tagsinput.component";
import {InputfileComponent} from "./Shared/MaterialComponent/inputfile/inputfile.component";
import {Dropdown2Component} from "./Shared/MaterialComponent/dropdown2/dropdown2.component";
import {PortletComponent} from "./Shared/MaterialComponent/Portlet/Portlet.Component";
import {InputDiscussionComponent} from "./Shared/MaterialComponent/InputDiscussion/InputDiscussion.component";
import {AuthGuard} from "./Auth.Guard.Service";
import {BottomToastsManager} from "./Shared/CustomToaster";
import {NgvasModule} from "./Shared/NgVas/ngvas.module";
// import {[MODULE]Service} from "./Modules/[MODULE]/[MODULE].Service";
//[END]

@NgModule({
    imports: [ToastModule.forRoot(), BrowserModule, NgvasModule, FormsModule, HttpModule, Routing, BrowserAnimationsModule, ReactiveFormsModule, NgbModule.forRoot(), ConfirmationPopoverModule.forRoot(),
        InputTextModule, CalendarModule, ButtonModule, DataTableModule, DialogModule, TreeModule, RatingModule, AccordionModule, ContextMenuModule],
    declarations: [AppComponent, HeaderComponent, BodyComponent, PagingComponent, DropdownComponent, ExcelComponent, TagsinputComponent,
        HomeComponent, InputfileComponent, ModalComponent, Dropdown2Component, PortletComponent,
        MenuPurchaseComponent, TooltipDirective, InputDiscussionComponent,
        // LayerAccessControlComponent, RuleComponent,
        //[IMPORT MODULE] [
        //[MODULE]Component,
        edgeComponent, fileComponent, pointComponent, problemComponent, ShapeComponent, userComponent,        //[MODULE]Component,
        //[END],DropdownComponent2
    ],
    providers: [
        AuthGuard,
        {
            provide: Http,
            useFactory: HttpFactory,
            deps: [XHRBackend, RequestOptions]
        },
        {
            provide: NgbDateParserFormatter,
            useClass: NgbDateFRParserFormatter
        },
        // {
        //     provide: AuthService,
        //     useFactory: AuthFactory,
        //     deps: [Http, RoleService]
        // },

        BottomToastsManager,
        //[IMPORT MODULE] [
        //[MODULE]Service,
        EdgeService, FileService, pointService, ProblemService, ShapeService, userService,        //[MODULE]Service,
        //[END]
    ], schemas: [NO_ERRORS_SCHEMA],

    bootstrap: [AppComponent]
})
export class AppModule {
}

export function HttpFactory(backend: XHRBackend, options: RequestOptions) {
    return new HttpService(backend, options);
}

// export function AuthFactory(Http: Http, RoleService: RoleService): AuthService {
//     return new AuthService(Http, RoleService);
// }

