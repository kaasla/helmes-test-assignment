interface ToastProps {
  readonly message: string;
  readonly type: "success" | "error";
  readonly visible: boolean;
}

const Toast = ({ message, type, visible }: ToastProps) => {
  if (!visible && !message) return null;

  const bgColor = type === "success" ? "bg-emerald-500" : "bg-red-500";

  return (
    <div
      className={`fixed right-6 bottom-6 z-50 rounded-lg px-5 py-3 text-sm font-medium text-white shadow-lg transition-all duration-300 ${bgColor} ${
        visible ? "translate-y-0 opacity-100" : "translate-y-2 opacity-0"
      }`}
    >
      {message}
    </div>
  );
};

export default Toast;
