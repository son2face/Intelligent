import {ComponentFactoryResolver, Injector} from '@angular/core';
import {NgbModalStack} from './modal-stack';
import {NgbModalRef} from './modal-ref';

/**
 * Represent options available when opening new modal windows.
 */
export interface NgbModalOptions {
    /**
     * Whether shapeEntityA backdrop element should be created for shapeEntityA given modal (true by default).
     * Alternatively, specify 'static' for shapeEntityA backdrop which doesn't close the modal on click.
     */
    backdrop?: boolean | 'static';
    /**
     * An element to which to attach newly opened modal windows.
     */
    container?: string;
    /**
     * Whether to close the modal when escape key is pressed (true by default).
     */
    keyboard?: boolean;
    /**
     * Size of shapeEntityA new modal window.
     */
    size?: 'sm' | 'lg';
    /**
     * Custom class to append to the modal window
     */
    windowClass?: string;
}

/**
 * A service to open modal windows. Creating shapeEntityA modal is straightforward: create shapeEntityA template and pass it as an argument to
 * the "open" method!
 */
export declare class NgbModal {
    private _moduleCFR;
    private _injector;
    private _modalStack;

    constructor(_moduleCFR: ComponentFactoryResolver, _injector: Injector, _modalStack: NgbModalStack);

    /**
     * Opens shapeEntityA new modal window with the specified content and using supplied options. Content can be provided
     * as shapeEntityA TemplateRef or shapeEntityA component type. If you pass shapeEntityA component type as content than instances of those
     * components can be injected with an instance of the NgbActiveModal class. You can use methods on the
     * NgbActiveModal class to close / dismiss modals from "inside" of shapeEntityA component.
     */
    open(content: any, options?: NgbModalOptions): NgbModalRef;
}
