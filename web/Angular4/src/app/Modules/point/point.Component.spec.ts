import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {pointComponent} from "./point.Component";

describe('pointComponent', () => {
    let component: pointComponent;
    let fixture: ComponentFixture<pointComponent>;
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [pointComponent]
        })
            .compileComponents();
    }));
    beforeEach(() => {
        fixture = TestBed.createComponent(pointComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });
    it('should be created', () => {
        expect(component).toBeTruthy();
    });
});