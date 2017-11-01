import {ApplicationRef, ComponentFactoryResolver, Injectable, NgZone} from "@angular/core";
import {ToastOptions, ToastsManager} from "ng2-toastr";

@Injectable()
export class BottomToastsManager extends ToastsManager {
    constructor(componentFactoryResolver: ComponentFactoryResolver, ngZone: NgZone, appRef: ApplicationRef, options: ToastOptions) {
        super(componentFactoryResolver, ngZone, appRef, Object.assign(options, {
            positionClass: "toast-bottom-right",
            toastLife: 3000,
            animate: 'fade',
            dismiss: 'auto'
        }));
    }

    ShowSuccess(message?: any) {
        if (typeof message !== 'string') {
            super.success('Thành công!', 'Hệ thống cập nhật');
        } else {
            super.success(message, 'Thành công');
        }
    }

    ShowWarning(message: any) {
        super.warning(message, 'Cảnh báo');
    }

    ShowError(message: any) {
        if (typeof message !== 'string') {
            super.error(JSON.parse(message._body).Message, 'Lỗi');
        } else {
            super.error(message, 'Lỗi');
        }
    }

    ShowInfo(message: any) {
        super.info(message, 'Thông báo');
    }
}