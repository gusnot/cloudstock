import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Branch } from './branch.model';
import { BranchPopupService } from './branch-popup.service';
import { BranchService } from './branch.service';

@Component({
    selector: 'jhi-branch-delete-dialog',
    templateUrl: './branch-delete-dialog.component.html'
})
export class BranchDeleteDialogComponent {

    branch: Branch;

    constructor(
        private branchService: BranchService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.branchService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'branchListModification',
                content: 'Deleted an branch'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-branch-delete-popup',
    template: ''
})
export class BranchDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private branchPopupService: BranchPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.branchPopupService
                .open(BranchDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
