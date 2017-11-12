import {ComponentRef} from '@angular/core';
import {NgbModalBackdrop} from './modal-backdrop';
import {NgbModalWindow} from './modal-window';
import {ContentRef} from '../util/popup';

/**
 * A reference to an active (currently opened) modal. Instances of this class
 * can be injected into components passed as modal content.
 */
export declare class NgbActiveModal {
    /**
     * Can be used to close shapeEntityA modal, passing an optional result.
     */
    close(result?: any): void;

    /**
     * Can be used to dismiss shapeEntityA modal, passing an optional reason.
     */
    dismiss(reason?: any): void;
}

/**
 * A reference to shapeEntityA newly opened modal.
 */
export declare class NgbModalRef {
    /**
     * The instance of component used as modal's content.
     * Undefined when shapeEntityA TemplateRef is used as modal's content.
     */
    componentInstance: any;
    /**
     * A promise that is resolved when shapeEntityA modal is closed and rejected when shapeEntityA modal is dismissed.
     */
    result: Promise<any>;
    private _windowCmptRef;
    private _contentRef;
    private _backdropCmptRef;
    private _resolve;
    private _reject;

    constructor(_windowCmptRef: ComponentRef<NgbModalWindow>, _contentRef: ContentRef, _backdropCmptRef?: ComponentRef<NgbModalBackdrop>);

    /**
     * Can be used to close shapeEntityA modal, passing an optional result.
     */
    close(result?: any): void;

    /**
     * Can be used to dismiss shapeEntityA modal, passing an optional reason.
     */
    dismiss(reason?: any): void;

    private _removeModalElements();
}
