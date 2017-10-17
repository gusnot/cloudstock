import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BillItem } from './bill-item.model';
import { BillItemPopupService } from './bill-item-popup.service';
import { BillItemService } from './bill-item.service';
import { Sku, SkuService } from '../sku';
import { Bill, BillService } from '../bill';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-bill-item-dialog',
    templateUrl: './bill-item-dialog.component.html'
})
export class BillItemDialogComponent implements OnInit {

    billItem: BillItem;
    isSaving: boolean;

    skus: Sku[];

    bills: Bill[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private billItemService: BillItemService,
        private skuService: SkuService,
        private billService: BillService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.skuService.query()
            .subscribe((res: ResponseWrapper) => { this.skus = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.billService.query()
            .subscribe((res: ResponseWrapper) => { this.bills = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.billItem.id !== undefined) {
            this.subscribeToSaveResponse(
                this.billItemService.update(this.billItem));
        } else {
            this.subscribeToSaveResponse(
                this.billItemService.create(this.billItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<BillItem>) {
        result.subscribe((res: BillItem) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: BillItem) {
        this.eventManager.broadcast({ name: 'billItemListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSkuById(index: number, item: Sku) {
        return item.id;
    }

    trackBillById(index: number, item: Bill) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-bill-item-popup',
    template: ''
})
export class BillItemPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private billItemPopupService: BillItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.billItemPopupService
                    .open(BillItemDialogComponent as Component, params['id']);
            } else {
                this.billItemPopupService
                    .open(BillItemDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
