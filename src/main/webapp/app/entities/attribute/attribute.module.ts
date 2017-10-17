import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    AttributeService,
    AttributePopupService,
    AttributeComponent,
    AttributeDetailComponent,
    AttributeDialogComponent,
    AttributePopupComponent,
    AttributeDeletePopupComponent,
    AttributeDeleteDialogComponent,
    attributeRoute,
    attributePopupRoute,
} from './';

const ENTITY_STATES = [
    ...attributeRoute,
    ...attributePopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AttributeComponent,
        AttributeDetailComponent,
        AttributeDialogComponent,
        AttributeDeleteDialogComponent,
        AttributePopupComponent,
        AttributeDeletePopupComponent,
    ],
    entryComponents: [
        AttributeComponent,
        AttributeDialogComponent,
        AttributePopupComponent,
        AttributeDeleteDialogComponent,
        AttributeDeletePopupComponent,
    ],
    providers: [
        AttributeService,
        AttributePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockAttributeModule {}
