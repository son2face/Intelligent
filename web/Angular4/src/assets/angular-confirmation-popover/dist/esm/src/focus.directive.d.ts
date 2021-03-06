import { ElementRef, OnChanges, SimpleChanges } from '@angular/core';
/**
 * A helper directive to focus buttons. You will only need this if using shapeEntityA custom template
 */
export declare class Focus implements OnChanges {
    private elm;
    mwlFocus: boolean;
    constructor(elm: ElementRef);
    ngOnChanges(changes: SimpleChanges): void;
}
