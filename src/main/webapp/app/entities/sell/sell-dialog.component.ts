import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Sell } from './sell.model';
import { SellPopupService } from './sell-popup.service';
import { SellService } from './sell.service';
import { Branch, BranchService } from '../branch';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sell-dialog',
    templateUrl: './sell-dialog.component.html'
})
export class SellDialogComponent implements OnInit {

    sell: Sell;
    isSaving: boolean;

    branches: Branch[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private sellService: SellService,
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
        if (this.sell.id !== undefined) {
            this.subscribeToSaveResponse(
                this.sellService.update(this.sell));
        } else {
            this.subscribeToSaveResponse(
                this.sellService.create(this.sell));
        }
    }

    private subscribeToSaveResponse(result: Observable<Sell>) {
        result.subscribe((res: Sell) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Sell) {
        this.eventManager.broadcast({ name: 'sellListModification', content: 'OK'});
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
    selector: 'jhi-sell-popup',
    template: ''
})
export class SellPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sellPopupService: SellPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.sellPopupService
                    .open(SellDialogComponent as Component, params['id']);
            } else {
                this.sellPopupService
                    .open(SellDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
