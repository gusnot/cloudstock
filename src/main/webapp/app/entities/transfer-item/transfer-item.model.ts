import { BaseEntity } from './../../shared';

export class TransferItem implements BaseEntity {
    constructor(
        public id?: number,
        public amount?: number,
        public skuId?: number,
        public transferId?: number,
    ) {
    }
}
