import {Component} from "@angular/core";
import {DropdownComponent} from "./Shared/MaterialComponent/dropdown/dropdown.component";
import {InputDiscussionComponent} from "./Shared/MaterialComponent/InputDiscussion/InputDiscussion.component";


@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    DropdownComponent : DropdownComponent = new DropdownComponent();
    DiscussionComponent : InputDiscussionComponent = new InputDiscussionComponent();
    constructor() {
    }



    ngOnInit() {

    }
}
