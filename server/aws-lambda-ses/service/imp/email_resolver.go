package imp

import _struct "golang/struct"

type EmailResolver interface {
	Type(string) bool
	Send(dto _struct.EmailStructureDTO)
}
