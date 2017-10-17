import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TransactionItem } from './transaction-item.model';
import { TransactionItemPopupService } from './transaction-item-popup.service';
import { TransactionItemService } from './transaction-item.service';
import { Sku, SkuService } from '../sku';
import { Transaction, TransactionService } from '../transaction';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-transaction-item-dialog',
    templateUrl: './transaction-item-dialog.component.html'
})
export class TransactionItemDialogComponent implements OnInit {

    transactionItem: TransactionItem;
    isSaving: boolean;

    skus: Sku[];

    transactions: Transaction[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private transactionItemService: TransactionItemService,
        private skuService: SkuService,
        private transactionService: TransactionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.skuService.query()
            .subscribe((res: ResponseWrapper) => { this.skus = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.transactionService.query()
            .subscribe((res: ResponseWrapper) => { this.transactions = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.transactionItem.id !== undefined) {
            this.subscribeToSaveResponse(
                this.transactionItemService.update(this.transactionItem));
        } else {
            this.subscribeToSaveResponse(
                this.transactionItemService.create(this.transactionItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<TransactionItem>) {
        result.subscribe((res: TransactionItem) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TransactionItem) {
        this.eventManager.broadcast({ name: 'transactionItemListModification', content: 'OK'});
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

    trackTransactionById(index: number, item: Transaction) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-transaction-item-popup',
    template: ''
})
export class TransactionItemPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transactionItemPopupService: TransactionItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.transactionItemPopupService
                    .open(TransactionItemDialogComponent as Component, params['id']);
            } else {
                this.transactionItemPopupService
                    .open(TransactionItemDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
