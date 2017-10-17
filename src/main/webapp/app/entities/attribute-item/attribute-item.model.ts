import { BaseEntity } from './../../shared';

export class AttributeItem implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public prefix?: string,
        public attributeId?: number,
    ) {
    }
}
