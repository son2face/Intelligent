import {isNullOrUndefined} from "util";

export class MenuModel {
    Name: string;
    Link: string;
    Sub: MenuModel[];

    constructor(Name: string, Link?: string, Sub?: Array<MenuModel>) {
        this.Name = Name;
        if (!isNullOrUndefined(Link)) {
            this.Link = Link;
        } else {
            this.Link = "";
        }
        if (!isNullOrUndefined(Sub)) {
            this.Sub = Sub;
        } else {
            this.Sub = [];
        }
    }

    addSub(Sub: MenuModel): void {
        this.Sub.push(Sub);
    }

    hasSub(): boolean {
        return (!(isNullOrUndefined(this.Sub) || this.Sub.length == 0));
    }
}
