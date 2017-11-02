import {Component, OnInit} from '@angular/core';
import {ProblemService} from "../problem/problem.Service";
import {ShapeService} from "../shape/shape.Service";
import {hitAreas, types} from "ngvas";
import Line = types.Line;

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    public problemEntity: any;
    public shapes: Array<Array<Line>> = [];
    public number = 0;

    // [[[0, 0], [100, 0] ], [[0, 0], [10, 17]]];
    constructor(private problemService: ProblemService, private shapeService: ShapeService,) {
        problemService.GetId("1").subscribe(problem => {
                this.problemEntity = problem;
                this.problemEntity.shapeEntities.forEach(shape => {
                    this.shapeService.GetId(shape.shapeId.toString()).subscribe(shapeEntity => {
                        shape.edgeEntities = shapeEntity.edgeEntities;
                        let t: Array<Line> = [];
                        shape.edgeEntities.forEach(edge => {
                            t.push([[edge.startX, edge.startY], [edge.endX, edge.endY]]);
                        });
                        this.shapes.push(t);
                    });
                });
            }
        );
    }

    public pixelHitArea = hitAreas.PixelHitArea;

    ngOnInit() {
    }

    public onDrag(e: MouseEvent, item): void {
        debugger;
        console.log(e);
        console.log(item);

        // this.squareTranslate = [[0, -100], 500, tweens.easings.easeOutCircular, () => this.squareTranslate = [[0, 100], 800, tweens.easings.easeOutBounce]];
    }

    Rotate(event, data) {
        debugger;
        data[0]
    }

    Test(event) {
        console.log(event);
        debugger;
    }
}
