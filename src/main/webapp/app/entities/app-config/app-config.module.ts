import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    AppConfigService,
    AppConfigPopupService,
    AppConfigComponent,
    AppConfigDetailComponent,
    AppConfigDialogComponent,
    AppConfigPopupComponent,
    AppConfigDeletePopupComponent,
    AppConfigDeleteDialogComponent,
    appConfigRoute,
    appConfigPopupRoute,
} from './';

const ENTITY_STATES = [
    ...appConfigRoute,
    ...appConfigPopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AppConfigComponent,
        AppConfigDetailComponent,
        AppConfigDialogComponent,
        AppConfigDeleteDialogComponent,
        AppConfigPopupComponent,
        AppConfigDeletePopupComponent,
    ],
    entryComponents: [
        AppConfigComponent,
        AppConfigDialogComponent,
        AppConfigPopupComponent,
        AppConfigDeleteDialogComponent,
        AppConfigDeletePopupComponent,
    ],
    providers: [
        AppConfigService,
        AppConfigPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockAppConfigModule {}
