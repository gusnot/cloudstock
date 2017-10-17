import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Bill } from './bill.model';
import { BillPopupService } from './bill-popup.service';
import { BillService } from './bill.service';
import { Branch, BranchService } from '../branch';
import { Sell, SellService } from '../sell';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-bill-dialog',
    templateUrl: './bill-dialog.component.html'
})
export class BillDialogComponent implements OnInit {

    bill: Bill;
    isSaving: boolean;

    branches: Branch[];

    sells: Sell[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private billService: BillService,
        private branchService: BranchService,
        private sellService: SellService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.branchService.query()
            .subscribe((res: ResponseWrapper) => { this.branches = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.sellService.query()
            .subscribe((res: ResponseWrapper) => { this.sells = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.bill.id !== undefined) {
            this.subscribeToSaveResponse(
                this.billService.update(this.bill));
        } else {
            this.subscribeToSaveResponse(
                this.billService.create(this.bill));
        }
    }

    private subscribeToSaveResponse(result: Observable<Bill>) {
        result.subscribe((res: Bill) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Bill) {
        this.eventManager.broadcast({ name: 'billListModification', content: 'OK'});
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

    trackSellById(index: number, item: Sell) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-bill-popup',
    template: ''
})
export class BillPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private billPopupService: BillPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.billPopupService
                    .open(BillDialogComponent as Component, params['id']);
            } else {
                this.billPopupService
                    .open(BillDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
