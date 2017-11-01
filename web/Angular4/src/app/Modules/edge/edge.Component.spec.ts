import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {edgeComponent} from "./edge.Component";

describe('edgeComponent', () => {
    let component: edgeComponent;
    let fixture: ComponentFixture<edgeComponent>;
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [edgeComponent]
        })
            .compileComponents();
    }));
    beforeEach(() => {
        fixture = TestBed.createComponent(edgeComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });
    it('should be created', () => {
        expect(component).toBeTruthy();
    });
});