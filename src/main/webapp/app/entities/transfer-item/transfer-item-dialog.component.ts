import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TransferItem } from './transfer-item.model';
import { TransferItemPopupService } from './transfer-item-popup.service';
import { TransferItemService } from './transfer-item.service';
import { Sku, SkuService } from '../sku';
import { Transfer, TransferService } from '../transfer';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-transfer-item-dialog',
    templateUrl: './transfer-item-dialog.component.html'
})
export class TransferItemDialogComponent implements OnInit {

    transferItem: TransferItem;
    isSaving: boolean;

    skus: Sku[];

    transfers: Transfer[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private transferItemService: TransferItemService,
        private skuService: SkuService,
        private transferService: TransferService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.skuService.query()
            .subscribe((res: ResponseWrapper) => { this.skus = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.transferService.query()
            .subscribe((res: ResponseWrapper) => { this.transfers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.transferItem.id !== undefined) {
            this.subscribeToSaveResponse(
                this.transferItemService.update(this.transferItem));
        } else {
            this.subscribeToSaveResponse(
                this.transferItemService.create(this.transferItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<TransferItem>) {
        result.subscribe((res: TransferItem) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TransferItem) {
        this.eventManager.broadcast({ name: 'transferItemListModification', content: 'OK'});
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

    trackTransferById(index: number, item: Transfer) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-transfer-item-popup',
    template: ''
})
export class TransferItemPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transferItemPopupService: TransferItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.transferItemPopupService
                    .open(TransferItemDialogComponent as Component, params['id']);
            } else {
                this.transferItemPopupService
                    .open(TransferItemDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
