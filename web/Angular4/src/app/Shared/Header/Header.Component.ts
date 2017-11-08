import {Component} from "@angular/core";
import {MenuModel} from "../../menu-model";
import {Http} from "@angular/http";
// import {AuthService} from "../../Modules/Auth/Auth.Service";
// import {UserEntity} from "../../Modules/User/User.Entity";
// import {UserService} from "../../Modules/User/User.Service";

// import {LayerAccessControlEntity} from "../../Modules/LayerAccessControl/LayerAccessControl.Entity";

@Component({
    selector: 'public-header',
    templateUrl: './Header.Component.html',
    styleUrls: ['./Header.Component.css']
})
export class HeaderComponent {
    MenuList: MenuModel[];
    // static User: UserEntity;
    // ParentLayerControl: LayerAccessControlEntity;
    // UserEntity: UserEntity;

    constructor(public Http: Http,
                // private UserService: UserService
                // , public AuthService: AuthService
    ) {
        // UserService.GetCurrent().subscribe(x => {
            // HeaderComponent.User = x;
            // this.UserService.UserEntity = x;
            // this.UserEntity = x;
        // });
        this.MenuList = Array<MenuModel>();
        let Home = new MenuModel("Trang chủ", "Home");
        let users = new MenuModel("Người dùng", "users");
        let shapes = new MenuModel("Mảnh ghép", "shapes");
        let problems = new MenuModel("Đề bài", "problems");
        let points = new MenuModel("Tọa độ điểm", "points");
        let files = new MenuModel("File", "files");
        let edges = new MenuModel("Cạnh", "edges");
        let board = new MenuModel("Bảng vẽ", "board");

        this.MenuList.push(Home);
        this.MenuList.push(problems);
        this.MenuList.push(shapes);
        this.MenuList.push(points);
        this.MenuList.push(files);
        this.MenuList.push(edges);
        this.MenuList.push(users);
        this.MenuList.push(board);
    }
}