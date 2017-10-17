import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AttributeItem } from './attribute-item.model';
import { AttributeItemPopupService } from './attribute-item-popup.service';
import { AttributeItemService } from './attribute-item.service';
import { Attribute, AttributeService } from '../attribute';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-attribute-item-dialog',
    templateUrl: './attribute-item-dialog.component.html'
})
export class AttributeItemDialogComponent implements OnInit {

    attributeItem: AttributeItem;
    isSaving: boolean;

    attributes: Attribute[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private attributeItemService: AttributeItemService,
        private attributeService: AttributeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.attributeService.query()
            .subscribe((res: ResponseWrapper) => { this.attributes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.attributeItem.id !== undefined) {
            this.subscribeToSaveResponse(
                this.attributeItemService.update(this.attributeItem));
        } else {
            this.subscribeToSaveResponse(
                this.attributeItemService.create(this.attributeItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<AttributeItem>) {
        result.subscribe((res: AttributeItem) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: AttributeItem) {
        this.eventManager.broadcast({ name: 'attributeItemListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackAttributeById(index: number, item: Attribute) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-attribute-item-popup',
    template: ''
})
export class AttributeItemPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private attributeItemPopupService: AttributeItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.attributeItemPopupService
                    .open(AttributeItemDialogComponent as Component, params['id']);
            } else {
                this.attributeItemPopupService
                    .open(AttributeItemDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
