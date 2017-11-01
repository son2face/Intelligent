import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {fileComponent} from "./file.Component";

describe('fileComponent', () => {
    let component: fileComponent;
    let fixture: ComponentFixture<fileComponent>;
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [fileComponent]
        })
            .compileComponents();
    }));
    beforeEach(() => {
        fixture = TestBed.createComponent(fileComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });
    it('should be created', () => {
        expect(component).toBeTruthy();
    });
});