import { BaseEntity } from './../../shared';

export class TransactionItem implements BaseEntity {
    constructor(
        public id?: number,
        public amount?: number,
        public skuId?: number,
        public transactionId?: number,
    ) {
    }
}
