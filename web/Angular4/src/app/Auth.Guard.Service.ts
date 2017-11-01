import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from '@angular/router';
// import {AuthService} from './Modules/Auth/Auth.Service';

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(
        // private authService: AuthService
    ) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        let url: string = state.url;
        return true;
//        return this.authService.GetTypeOfLayout('',url).Ty == 'Show';
    }
}