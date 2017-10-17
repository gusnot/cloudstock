import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Transfer } from './transfer.model';
import { TransferPopupService } from './transfer-popup.service';
import { TransferService } from './transfer.service';
import { Branch, BranchService } from '../branch';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-transfer-dialog',
    templateUrl: './transfer-dialog.component.html'
})
export class TransferDialogComponent implements OnInit {

    transfer: Transfer;
    isSaving: boolean;

    branches: Branch[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private transferService: TransferService,
        private branchService: BranchService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.branchService.query()
            .subscribe((res: ResponseWrapper) => { this.branches = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.transfer.id !== undefined) {
            this.subscribeToSaveResponse(
                this.transferService.update(this.transfer));
        } else {
            this.subscribeToSaveResponse(
                this.transferService.create(this.transfer));
        }
    }

    private subscribeToSaveResponse(result: Observable<Transfer>) {
        result.subscribe((res: Transfer) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Transfer) {
        this.eventManager.broadcast({ name: 'transferListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBranchById(index: number, item: Branch) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-transfer-popup',
    template: ''
})
export class TransferPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transferPopupService: TransferPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.transferPopupService
                    .open(TransferDialogComponent as Component, params['id']);
            } else {
                this.transferPopupService
                    .open(TransferDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
