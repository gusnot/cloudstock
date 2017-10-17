import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Stock } from './stock.model';
import { StockPopupService } from './stock-popup.service';
import { StockService } from './stock.service';
import { Branch, BranchService } from '../branch';
import { Sku, SkuService } from '../sku';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-stock-dialog',
    templateUrl: './stock-dialog.component.html'
})
export class StockDialogComponent implements OnInit {

    stock: Stock;
    isSaving: boolean;

    branches: Branch[];

    skus: Sku[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private stockService: StockService,
        private branchService: BranchService,
        private skuService: SkuService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.branchService.query()
            .subscribe((res: ResponseWrapper) => { this.branches = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.skuService.query()
            .subscribe((res: ResponseWrapper) => { this.skus = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.stock.id !== undefined) {
            this.subscribeToSaveResponse(
                this.stockService.update(this.stock));
        } else {
            this.subscribeToSaveResponse(
                this.stockService.create(this.stock));
        }
    }

    private subscribeToSaveResponse(result: Observable<Stock>) {
        result.subscribe((res: Stock) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Stock) {
        this.eventManager.broadcast({ name: 'stockListModification', content: 'OK'});
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

    trackSkuById(index: number, item: Sku) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-stock-popup',
    template: ''
})
export class StockPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stockPopupService: StockPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.stockPopupService
                    .open(StockDialogComponent as Component, params['id']);
            } else {
                this.stockPopupService
                    .open(StockDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
