import {FilterEntity} from "../../Shared/Filter.Entity";

export class SearchuserEntity extends FilterEntity {
    userId: number;
    userName: string;
    passWord: string;

    constructor(user: any = null) {
        super(user);
        this.userId = user == null ? null : user.userId;
        this.userName = user == null ? null : user.userName;
        this.passWord = user == null ? null : user.passWord;
    }
}