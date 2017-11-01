import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {ShapeComponent} from "./shape.Component";

describe('ShapeComponent', () => {
    let component: ShapeComponent;
    let fixture: ComponentFixture<ShapeComponent>;
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [ShapeComponent]
        })
            .compileComponents();
    }));
    beforeEach(() => {
        fixture = TestBed.createComponent(ShapeComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });
    it('should be created', () => {
        expect(component).toBeTruthy();
    });
});