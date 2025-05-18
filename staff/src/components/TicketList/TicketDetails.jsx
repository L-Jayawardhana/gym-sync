import React, { useState, useEffect } from 'react';
import StatusBadge from './StatusBadge';
import StatusChangePreview from './StatusChangePreview';

const getAssignedToInfo = (ticket) => {
  if (!ticket.assignedToId && !ticket.assignedToName) {
    return 'Not assigned';
  }
  
  const name = ticket.assignedToName || 'Unknown';
  const id = ticket.assignedToId || 'N/A';
  
  return `${name} (Staff ID: ${id})`;
};

const TicketDetails = ({ 
  ticket, 
  handleStatusChange, 
  selectedStatuses, 
  loading,
  getTicketId,
  formatDate
}) => {
  const [showFullDescription, setShowFullDescription] = useState(false);
  const [localStatus, setLocalStatus] = useState("");

  useEffect(() => {
    setLocalStatus(selectedStatuses[getTicketId(ticket)] || "");
  }, [selectedStatuses, getTicketId, ticket]);

  const toggleDescription = () => {
    setShowFullDescription(!showFullDescription);
  };

  const shortenedDescription = ticket.description && ticket.description.length > 150 
    ? `${ticket.description.substring(0, 150)}...` 
    : ticket.description;

  const onStatusChange = (e) => {
    const newStatus = e.target.value;
    setLocalStatus(newStatus);
    handleStatusChange(getTicketId(ticket), newStatus);
  };

  return (
    <div className="bg-gray-50 p-6 shadow-inner border-t border-gray-200">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* First column - Ticket Information */}
        <div className="bg-white p-4 rounded-lg shadow-sm border border-gray-100">
          <h4 className="text-sm font-semibold text-gray-700 mb-3 pb-2 border-b border-gray-200">Ticket Information</h4>
          <div className="space-y-2">
            <p className="flex justify-between">
              <span className="text-sm font-medium text-gray-500">ID:</span>
              <span className="text-sm text-gray-900">{getTicketId(ticket)}</span>
            </p>
            <p className="flex justify-between">
              <span className="text-sm font-medium text-gray-500">Type:</span>
              <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                {ticket.type}
              </span>
            </p>
            <p className="flex justify-between">
              <span className="text-sm font-medium text-gray-500">Status:</span>
              <StatusBadge status={ticket.status} />
            </p>
            <p className="flex justify-between">
              <span className="text-sm font-medium text-gray-500">Priority:</span>
              <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium 
                ${ticket.priority === 'HIGH' ? 'bg-red-100 text-red-800' : 
                  ticket.priority === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' : 
                  'bg-green-100 text-green-800'}`}>
                {ticket.priority}
              </span>
            </p>
            <p className="flex justify-between">
              <span className="text-sm font-medium text-gray-500">Created:</span>
              <span className="text-sm text-gray-900">{formatDate(ticket.createdAt)}</span>
            </p>
            {ticket.updatedAt && (
              <p className="flex justify-between">
                <span className="text-sm font-medium text-gray-500">Updated:</span>
                <span className="text-sm text-gray-900">{formatDate(ticket.updatedAt)}</span>
              </p>
            )}
          </div>

          <div className="mt-4 pt-3 border-t border-gray-100">
            <h5 className="text-sm font-semibold text-gray-700 mb-2">Description:</h5>
            <div className="relative">
              <p className="text-sm text-gray-800 bg-gray-50 p-3 rounded-md">
                {showFullDescription ? ticket.description : shortenedDescription}
              </p>
              {ticket.description && ticket.description.length > 150 && (
                <button 
                  onClick={toggleDescription} 
                  className="mt-1 text-xs text-rose-600 hover:text-rose-800 font-medium focus:outline-none"
                >
                  {showFullDescription ? 'Show less' : 'Read more'}
                </button>
              )}
            </div>
          </div>

        </div>

        {/* Second column - User & Assignment */}
        <div className="bg-white p-4 rounded-lg shadow-sm border border-gray-100">
          <h4 className="text-sm font-semibold text-gray-700 mb-3 pb-2 border-b border-gray-200">Assignment</h4>
          <div className="space-y-2">
            <p className="flex justify-between">
              <span className="text-sm font-medium text-gray-500">Assigned To:</span>
              <span className="text-sm text-gray-900">{getAssignedToInfo(ticket)}</span>
            </p>
          </div>

            {/* Status Progress Visualization */}
          <div className="mt-4 pt-3 border-t border-gray-100">
            <h5 className="text-sm font-semibold text-gray-700 mb-2">Status Progress:</h5>
            <StatusChangePreview currentStatus={ticket.status} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default TicketDetails;
