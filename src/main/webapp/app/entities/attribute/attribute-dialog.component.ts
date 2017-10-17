import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Attribute } from './attribute.model';
import { AttributePopupService } from './attribute-popup.service';
import { AttributeService } from './attribute.service';

@Component({
    selector: 'jhi-attribute-dialog',
    templateUrl: './attribute-dialog.component.html'
})
export class AttributeDialogComponent implements OnInit {

    attribute: Attribute;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private attributeService: AttributeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.attribute.id !== undefined) {
            this.subscribeToSaveResponse(
                this.attributeService.update(this.attribute));
        } else {
            this.subscribeToSaveResponse(
                this.attributeService.create(this.attribute));
        }
    }

    private subscribeToSaveResponse(result: Observable<Attribute>) {
        result.subscribe((res: Attribute) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Attribute) {
        this.eventManager.broadcast({ name: 'attributeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-attribute-popup',
    template: ''
})
export class AttributePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private attributePopupService: AttributePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.attributePopupService
                    .open(AttributeDialogComponent as Component, params['id']);
            } else {
                this.attributePopupService
                    .open(AttributeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
