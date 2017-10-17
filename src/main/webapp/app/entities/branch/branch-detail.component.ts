import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Branch } from './branch.model';
import { BranchService } from './branch.service';

@Component({
    selector: 'jhi-branch-detail',
    templateUrl: './branch-detail.component.html'
})
export class BranchDetailComponent implements OnInit, OnDestroy {

    branch: Branch;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private branchService: BranchService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBranches();
    }

    load(id) {
        this.branchService.find(id).subscribe((branch) => {
            this.branch = branch;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBranches() {
        this.eventSubscriber = this.eventManager.subscribe(
            'branchListModification',
            (response) => this.load(this.branch.id)
        );
    }
}
