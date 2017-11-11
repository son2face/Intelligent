import {FilterEntity} from "../../Shared/Filter.Entity";

export class SearchProblemEntity extends FilterEntity {
    problemId: number;
    status: string;
    fileId: string;
    userId: string;

    constructor(problem: any = null) {
        super(problem);
        this.problemId = problem == null ? null : problem.problemId;
        this.status = problem == null ? null : problem.status;
        this.fileId = problem == null ? null : problem.fileId;
        this.userId = problem == null ? null : problem.userId;
    }
}