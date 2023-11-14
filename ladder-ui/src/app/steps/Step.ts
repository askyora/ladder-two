export interface Step{
    id: number;
    name: string;
    description: string;
    address: string;
    clientId: number;
    inheritable :boolean;
    overridable :boolean;
    parentId: number;
}