import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {problemComponent} from "./problem.Component";

describe('problemComponent', () => {
    let component: problemComponent;
    let fixture: ComponentFixture<problemComponent>;
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [problemComponent]
        })
            .compileComponents();
    }));
    beforeEach(() => {
        fixture = TestBed.createComponent(problemComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });
    it('should be created', () => {
        expect(component).toBeTruthy();
    });
});