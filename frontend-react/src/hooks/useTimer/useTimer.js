// import moment, { MomentInput } from 'moment';
// import { useRef, useEffect, useState, useCallback } from 'react';
export {};
// import { diff, now } from '@utils/date';
// const useTimer = ({
// 	startDate,
// 	time,
// 	limit,
// 	autostart
// }: {
// 	startDate?: MomentInput;
// 	time?: number;
// 	limit?: number;
// 	autostart?: boolean;
// }) => {
// 	const intervalRef = useRef<ReturnType<typeof setInterval>>(null);
// 	const [seconds, setSeconds] = useState<number>(time || diff(now().format(), moment(startDate), 'seconds'));
// 	const getParsedTime = useCallback((currentSeconds: number): string => {
// 		const secondsValue = Math.floor(currentSeconds % 60) || 0;
// 		const minutes = Math.floor((currentSeconds / 60) % 60) || 0;
// 		const hours = Math.floor(currentSeconds / 3600) || 0;
// 		let parsedSeconds = secondsValue < 10 ? `0${secondsValue}` : secondsValue;
// 		let parsedMinutes = minutes < 10 ? `0${minutes}` : minutes;
// 		let parsedHours = hours < 10 ? `0${hours}` : hours;
// 		return `${parsedHours}:${parsedMinutes}:${parsedSeconds}`;
// 	}, []);
// 	const stop = useCallback(() => {
// 		clearInterval(intervalRef.current);
// 		setSeconds(0);
// 	}, [intervalRef]);
// 	const pause = useCallback(() => {
// 		clearInterval(intervalRef.current);
// 	}, [intervalRef]);
// 	const start = useCallback(() => {
// 		intervalRef.current = setInterval(() => {
// 			setSeconds(currentSeconds => {
// 				if (limit && limit === currentSeconds) {
// 					pause();
// 					return currentSeconds;
// 				}
// 				return currentSeconds + 1;
// 			});
// 		}, 1000);
// 	}, [limit, pause, intervalRef]);
// 	const restart = useCallback(() => {
// 		setSeconds(0);
// 		start();
// 	}, [start]);
// 	useEffect(() => {
// 		if (autostart) {
// 			start();
// 		}
// 	}, [autostart, start]);
// 	return {
// 		start,
// 		stop,
// 		pause,
// 		restart,
// 		seconds,
// 		parsedTime: getParsedTime(seconds)
// 	};
// };
// export default useTimer;
//# sourceMappingURL=useTimer.js.map